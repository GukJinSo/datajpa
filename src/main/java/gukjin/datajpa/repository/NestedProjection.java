package gukjin.datajpa.repository;

public interface NestedProjection {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
