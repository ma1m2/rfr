package io.student.rangiffler.data;

import java.util.ArrayList;
import java.util.List;

import static io.student.rangiffler.utils.RandomDataUtils.randomPassword;
import static io.student.rangiffler.utils.RandomDataUtils.randomUsername;

public final class TestUser {
  private final String username;
  private final String password;
  private final List<TestUser> friends;
  private final List<TestUser> incomeInvitations;

  public TestUser(String username, String password) {
    this(username, password, List.of(), List.of());
  }

  public TestUser(String username, String password, List<TestUser> friends, List<TestUser> incomeInvitations) {
    this.username = username;
    this.password = password;
    this.friends = List.copyOf(friends);
    this.incomeInvitations = List.copyOf(incomeInvitations);
  }

  public static TestUser unique() {
    return unique(0, 0);
  }

  public static TestUser unique(int friends, int incomeInvitations) {
    return new TestUser(randomUsername(), randomPassword(), users(friends), users(incomeInvitations));
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }

  public List<TestUser> friends() {
    return friends;
  }

  public List<TestUser> incomeInvitations() {
    return incomeInvitations;
  }

  private static List<TestUser> users(int count) {
    List<TestUser> users = new ArrayList<>(count);
    for (int index = 0; index < count; index++) {
      users.add(unique());
    }
    return users;
  }
}
