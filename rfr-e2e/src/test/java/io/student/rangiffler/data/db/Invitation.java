package io.student.rangiffler.data.db;

public final class Invitation implements Stored {
  private final String requester;
  private final String addressee;

  public Invitation(String requester, String addressee) {
    this.requester = requester;
    this.addressee = addressee;
  }

  @Override
  public void create() {
    int updated = Jdbc.client(Databases.API)
        .sql(Jdbc.sql("sql/api/create-invitation.sql"))
        .param("requester", requester)
        .param("addressee", addressee)
        .update();
    if (updated != 1) {
      throw new IllegalStateException(
          "Expected one inserted invitation from " + requester + " to " + addressee + ", got " + updated
      );
    }
  }

  @Override
  public void delete() {
    Jdbc.client(Databases.API)
        .sql(Jdbc.sql("sql/api/delete-friendship.sql"))
        .param("requester", requester)
        .param("addressee", addressee)
        .param("status", FriendshipStatus.PENDING.name())
        .update();
  }

  @Override
  public boolean exists() {
    return Jdbc.client(Databases.API)
        .sql(Jdbc.sql("sql/api/exists-friendship.sql"))
        .param("requester", requester)
        .param("addressee", addressee)
        .param("status", FriendshipStatus.PENDING.name())
        .query((rs, rowNum) -> rs.getInt(1))
        .single() > 0;
  }
}
