package io.student.rangiffler.data;

import io.qameta.allure.Step;
import io.student.rangiffler.data.db.Invitation;
import io.student.rangiffler.data.db.Invitations;

import java.util.ArrayList;
import java.util.List;

public final class UserWithIncomeInvitations implements UserFixture {
  private final UserFixture origin;
  private final List<UserFixture> invitations;

  public UserWithIncomeInvitations(UserFixture origin) {
    this.origin = origin;
    this.invitations = fixtures(origin.user().incomeInvitations());
  }

  @Override
  public TestUser user() {
    return origin.user();
  }

  @Override
  public void create() {
    origin.create();
    for (UserFixture invitation : invitations) {
      invitation.create();
      link(invitation);
    }
  }

  @Step("Delete income invitations of created user")
  @Override
  public void close() {
    new TearDown()
        .with(this::unlink)
        .withEach(invitations, UserFixture::close)
        .with(origin::close)
        .close();
  }

  @Step("Create income invitation from {requester.user.username}")
  private void link(UserFixture requester) {
    new Invitation(requester.user().username(), origin.user().username()).create();
  }

  private void unlink() {
    new Invitations(origin.user().username()).delete();
  }

  private static List<UserFixture> fixtures(List<TestUser> users) {
    List<UserFixture> fixtures = new ArrayList<>(users.size());
    for (TestUser user : users) {
      fixtures.add(new SingleUserFixture(user));
    }
    return List.copyOf(fixtures);
  }
}
