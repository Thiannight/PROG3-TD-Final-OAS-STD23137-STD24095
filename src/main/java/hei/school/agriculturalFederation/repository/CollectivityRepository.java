package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CollectivityRepository {

    private final DataSourceConfig dataSourceConfig;
    private final MemberRepository memberRepository;

    public CollectivityRepository(DataSourceConfig dataSourceConfig, MemberRepository memberRepository) {
        this.dataSourceConfig = dataSourceConfig;
        this.memberRepository = memberRepository;
    }

    public boolean existsById(String id) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM collectivity WHERE id = ?")) {
            ps.setString(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error in existsById collectivity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public Optional<Collectivity> findById(String id) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM collectivity WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error in findById collectivity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public boolean nameExistsForOther(String name, String excludeId) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM collectivity WHERE name = ? AND id != ?")) {
            ps.setString(1, name);
            ps.setString(2, excludeId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error in nameExistsForOther: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public boolean numberExistsForOther(String number, String excludeId) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM collectivity WHERE number = ? AND id != ?")) {
            ps.setString(1, number);
            ps.setString(2, excludeId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error in numberExistsForOther: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public Collectivity assignIdentity(String collectivityId, String number, String name) {
        StringBuilder sql = new StringBuilder("UPDATE collectivity SET ");
        boolean first = true;
        if (number != null) {
            sql.append("number = ?");
            first = false;
        }
        if (name != null) {
            if (!first) sql.append(", ");
            sql.append("name = ?");
        }
        sql.append(" WHERE id = ?");

        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (number != null) ps.setString(idx++, number);
            if (name != null)   ps.setString(idx++, name);
            ps.setString(idx, collectivityId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error in assignIdentity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
        return findById(collectivityId).orElseThrow();
    }

    private Collectivity mapRow(ResultSet rs) throws SQLException {
        Collectivity c = new Collectivity();
        c.setId(rs.getString("id"));
        c.setNumber(rs.getString("number"));   // nullable
        c.setName(rs.getString("name"));       // nullable
        c.setLocation(rs.getString("location"));

        CollectivityStructure structure = new CollectivityStructure();
        String presidentId     = rs.getString("president_id");
        String vicePresidentId = rs.getString("vice_president_id");
        String treasurerId     = rs.getString("treasurer_id");
        String secretaryId     = rs.getString("secretary_id");

        if (presidentId != null)
            memberRepository.findById(presidentId).ifPresent(structure::setPresident);
        if (vicePresidentId != null)
            memberRepository.findById(vicePresidentId).ifPresent(structure::setVicePresident);
        if (treasurerId != null)
            memberRepository.findById(treasurerId).ifPresent(structure::setTreasurer);
        if (secretaryId != null)
            memberRepository.findById(secretaryId).ifPresent(structure::setSecretary);

        c.setStructure(structure);
        c.setMembers(memberRepository.findAllByCollectivityId(c.getId()));
        return c;
    }

    public Collectivity save(String id,
                             String location,
                             boolean federationApproval,
                             String presidentId,
                             String vicePresidentId,
                             String treasurerId,
                             String secretaryId) {
        String sql = """
                INSERT INTO collectivity
                  (id, number, name, location, agricultural_specialty, creation_date,
                   federation_approval, president_id, vice_president_id, treasurer_id, secretary_id)
                VALUES (?, NULL, NULL, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, location);
            ps.setString(3, "Not defined");
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setBoolean(5, federationApproval);
            ps.setString(6, presidentId);
            ps.setString(7, vicePresidentId);
            ps.setString(8, treasurerId);
            ps.setString(9, secretaryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error in save collectivity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
        return findById(id).orElseThrow();
    }

    public void updateMemberCollectivity(List<String> memberIds, String collectivityId) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE member SET collectivity_id = ? WHERE id = ?")) {
            for (String memberId : memberIds) {
                ps.setString(1, collectivityId);
                ps.setString(2, memberId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error in updateMemberCollectivity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }
}