package gukjin.datajpa.repository;

public class UsernameOnlyDto {
    private final String username;

    public UsernameOnlyDto(String username) { // 파라미터 이름이 일치해야 함
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
