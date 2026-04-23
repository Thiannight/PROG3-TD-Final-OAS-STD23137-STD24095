package hei.school.agriculturalFederation.repository;

import hei.school.agriculturalFederation.datasource.DataSourceConfig;
import hei.school.agriculturalFederation.model.Gender;
import hei.school.agriculturalFederation.model.Member;
import hei.school.agriculturalFederation.model.MemberOccupation;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {

    private final DataSourceConfig dataSourceConfig;

    public MemberRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    // ------------------------------------------------------------------
    // Row mapping
    // ------------------------------------------------------------------

    private Member mapRow(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getString("id"));
        m.setFirstName(rs.getString("first_name"));
        m.setLastName(rs.getString("last_name"));
        m.setBirthDate(rs.getObject("birth_date", LocalDate.class));
        m.setGender(Gender.valueOf(rs.getString("gender")));
        m.setAddress(rs.getString("address"));
        m.setProfession(rs.getString("profession"));
        m.setPhoneNumber(rs.getLong("phone_number"));
        m.setEmail(rs.getString("email"));
        m.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));
        m.setMembershipDate(rs.getObject("adhesion_date", LocalDate.class));
        m.setCollectivityId(rs.getString("collectivity_id"));
        return m;
    }

    // ------------------------------------------------------------------
    // Queries
    // ------------------------------------------------------------------

    public Optional<Member> findById(String id) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM member WHERE id = ?")) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Member m = mapRow(rs);
                m.setReferees(findRefereesByMemberId(m.getId(), conn));
                return Optional.of(m);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error in findById member: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public List<Member> findAllByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        List<Member> members = new ArrayList<>();
        String placeholders = String.join(",", ids.stream().map(i -> "?").toList());
        String sql = "SELECT * FROM member WHERE id IN (" + placeholders + ")";
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setString(i + 1, ids.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Member m = mapRow(rs);
                m.setReferees(findRefereesByMemberId(m.getId(), conn));
                members.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error in findAllByIds member: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
        return members;
    }

    /**
     * Returns all members that belong to the given collectivity.
     * Uses the member_collectivity junction table so that members who
     * participate in multiple collectivities (e.g. C1-M1 in both col-1
     * and col-2) are correctly included for each.
     * The occupation returned reflects their role IN that specific collectivity.
     */
    public List<Member> findAllByCollectivityId(String collectivityId) {
        // Join member_collectivity so we get the right occupation per collectivity
        String sql = """
                SELECT m.*, mc.occupation AS col_occupation, mc.adhesion_date AS col_adhesion_date
                FROM member m
                JOIN member_collectivity mc ON mc.member_id = m.id
                WHERE mc.collectivity_id = ?
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                Member m = mapRow(rs);
                // Override occupation and adhesion date with the collectivity-specific values
                m.setOccupation(MemberOccupation.valueOf(rs.getString("col_occupation")));
                m.setMembershipDate(rs.getObject("col_adhesion_date", LocalDate.class));
                m.setCollectivityId(collectivityId);
                m.setReferees(findRefereesByMemberId(m.getId(), conn));
                members.add(m);
            }
            return members;
        } catch (SQLException e) {
            throw new RuntimeException("Error in findAllByCollectivityId: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    private List<Member> findRefereesByMemberId(String memberId, Connection conn) throws SQLException {
        String sql = """
                SELECT m.* FROM member m
                JOIN sponsorship s ON s.sponsor_id = m.id
                WHERE s.candidate_id = ?
                """;
        List<Member> referees = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                referees.add(mapRow(rs));
            }
        }
        return referees;
    }

    // ------------------------------------------------------------------
    // Persistence
    // ------------------------------------------------------------------

    public Member save(Member member) {
        String sql = """
                INSERT INTO member
                  (id, collectivity_id, first_name, last_name, birth_date, gender,
                   address, profession, phone_number, email, adhesion_date, occupation,
                   registration_fee_paid, membership_dues_paid)
                VALUES (?, ?, ?, ?, ?, CAST(? AS gender_enum), ?, ?, ?, ?, ?, CAST(? AS occupation_enum), ?, ?)
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getId());
            ps.setString(2, member.getCollectivityId());
            ps.setString(3, member.getFirstName());
            ps.setString(4, member.getLastName());
            ps.setDate(5, Date.valueOf(member.getBirthDate()));
            ps.setString(6, member.getGender().name());
            ps.setString(7, member.getAddress());
            ps.setString(8, member.getProfession());
            ps.setLong(9, member.getPhoneNumber());
            ps.setString(10, member.getEmail());
            ps.setDate(11, Date.valueOf(member.getMembershipDate()));
            ps.setString(12, member.getOccupation().name());
            // Use actual values from the member object instead of hardcoding true
            ps.setBoolean(13, member.isRegistrationFeePaid());
            ps.setBoolean(14, member.isMembershipDuesPaid());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error in save member: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }

        // Also insert into member_collectivity junction table
        saveMemberCollectivity(member.getId(), member.getCollectivityId(),
                member.getOccupation(), member.getMembershipDate(), conn);

        return member;
    }

    private void saveMemberCollectivity(String memberId, String collectivityId,
                                        MemberOccupation occupation, LocalDate adhesionDate,
                                        Connection ignoredConn) {
        String sql = """
                INSERT INTO member_collectivity (member_id, collectivity_id, occupation, adhesion_date)
                VALUES (?, ?, CAST(? AS occupation_enum), ?)
                ON CONFLICT (member_id, collectivity_id) DO NOTHING
                """;
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, memberId);
            ps.setString(2, collectivityId);
            ps.setString(3, occupation.name());
            ps.setDate(4, Date.valueOf(adhesionDate));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error in saveMemberCollectivity: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public void saveSponsorship(String candidateId, String sponsorId) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sponsorship (candidate_id, sponsor_id, relationship_nature) VALUES (?, ?, ?)")) {
            ps.setString(1, candidateId);
            ps.setString(2, sponsorId);
            ps.setString(3, "Not specified");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error in saveSponsorship: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public boolean collectivityExists(String collectivityId) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM collectivity WHERE id = ?")) {
            ps.setString(1, collectivityId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException("Error in collectivityExists: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }

    public long getCollectivityAnnualDues(String collectivityId) {
        Connection conn = dataSourceConfig.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT annual_dues FROM collectivity WHERE id = ?")) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("annual_dues");
            }
            throw new RuntimeException("Collectivity not found when reading annual_dues: " + collectivityId);
        } catch (SQLException e) {
            throw new RuntimeException("Error in getCollectivityAnnualDues: " + e.getMessage(), e);
        } finally {
            dataSourceConfig.closeConnection(conn);
        }
    }
}