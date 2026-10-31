package io.student.rangiffler.data;

public final class UserRequest {
  private final int friends;
  private final int incomeInvitations;

  public UserRequest(int friends, int incomeInvitations) {
    this.friends = nonNegative(friends, "friends");
    this.incomeInvitations = nonNegative(incomeInvitations, "incomeInvitations");
  }

  public int friends() {
    return friends;
  }

  public int incomeInvitations() {
    return incomeInvitations;
  }

  public UserFixture fixture() {
    return new UserWithIncomeInvitations(
        new UserWithFriendsFixture(
            new SingleUserFixture(TestUser.unique(friends, incomeInvitations))
        )
    );
  }

  private static int nonNegative(int value, String name) {
    if (value < 0) {
      throw new IllegalArgumentException("Expected non-negative " + name + ", got " + value);
    }
    return value;
  }
}
