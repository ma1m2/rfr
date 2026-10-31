package io.student.rangiffler.data;

import io.qameta.allure.Step;
import io.student.rangiffler.data.db.Friendship;
import io.student.rangiffler.data.db.Friendships;

import java.util.ArrayList;
import java.util.List;

public final class UserWithFriendsFixture implements UserFixture {
  private final UserFixture origin;
  private final List<UserFixture> friends;

  public UserWithFriendsFixture(UserFixture origin) {
    this.origin = origin;
    this.friends = fixtures(origin.user().friends());
  }

  @Override
  public TestUser user() {
    return origin.user();
  }

  @Override
  public void create() {
    origin.create();
    for (UserFixture friend : friends) {
      friend.create();
      link(friend);
    }
  }

  @Step("Delete friends of created user")
  @Override
  public void close() {
    new TearDown()
        .with(this::unlink)
        .withEach(friends, UserFixture::close)
        .with(origin::close)
        .close();
  }

  @Step("Create friendship with {friend.user.username}")
  private void link(UserFixture friend) {
    new Friendship(origin.user().username(), friend.user().username()).create();
  }

  private void unlink() {
    new Friendships(origin.user().username()).delete();
  }

  private static List<UserFixture> fixtures(List<TestUser> users) {
    List<UserFixture> fixtures = new ArrayList<>(users.size());
    for (TestUser user : users) {
      fixtures.add(new SingleUserFixture(user));
    }
    return List.copyOf(fixtures);
  }
}
