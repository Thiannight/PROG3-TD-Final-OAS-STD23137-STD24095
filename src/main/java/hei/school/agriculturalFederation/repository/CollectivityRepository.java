package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.model.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class CollectivityRepository {

    private final DataSource dataSource;
    private final MemberRepository memberRepository;

    public CollectivityRepository(DataSource dataSource, MemberRepository memberRepository) {
        this.dataSource = dataSource;
        this.memberRepository = memberRepository;
    }

    public boolean existsById(String id) {
        String sql = "SELECT 1 FROM collectivity WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error in existsById collectivity: " + e.getMessage(), e);
        }
    }

    public Optional<Collectivity> findById(String id) {
        String sql = "SELECT * FROM collectivity WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error in findById collectivity: " + e.getMessage(), e);
        }
    }

    private Collectivity mapRow(ResultSet rs) throws SQLException {
        Collectivity c = new Collectivity();
        c.setId(rs.getString("id"));
        c.setLocation(rs.getString("location"));

        CollectivityStructure structure = new CollectivityStructure();
        String presidentId      = rs.getString("president_id");
        String vicePresidentId  = rs.getString("vice_president_id");
        String treasurerId      = rs.getString("treasurer_id");
        String secretaryId      = rs.getString("secretary_id");

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
                  (id, name, location, agricultural_specialty, creation_date,
                   federation_approval, president_id, vice_president_id, treasurer_id, secretary_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, "Collectivity-" + id);
            ps.setString(3, location);
            ps.setString(4, "Not defined");
            ps.setDate(5, Date.valueOf(LocalDate.now()));
            ps.setBoolean(6, federationApproval);
            ps.setString(7, presidentId);
            ps.setString(8, vicePresidentId);
            ps.setString(9, treasurerId);
            ps.setString(10, secretaryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error in save collectivity: " + e.getMessage(), e);
        }
        return findById(id).orElseThrow();
    }

    public void updateMemberCollectivity(List<String> memberIds, String collectivityId) {
        String sql = "UPDATE member SET collectivity_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String memberId : memberIds) {
                ps.setString(1, collectivityId);
                ps.setString(2, memberId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Error in updateMemberCollectivity: " + e.getMessage(), e);
        }
    }
}