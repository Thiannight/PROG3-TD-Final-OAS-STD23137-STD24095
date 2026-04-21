package hei.school.agriculturalFederation.model;

import java.util.List;

public class Collectivity {
    private String id;
    private String location;
    private CollectivityStructure structure;
    private List<Member> members;

    public Collectivity() {}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public CollectivityStructure getStructure() {
        return structure;
    }
    public void setStructure(CollectivityStructure structure) {
        this.structure = structure;
    }

    public List<Member> getMembers() {
        return members;
    }
    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
