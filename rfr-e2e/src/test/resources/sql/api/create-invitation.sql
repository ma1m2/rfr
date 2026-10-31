INSERT INTO `friendship` (
  id,
  requester_id,
  addressee_id,
  status,
  created_at,
  version
)
SELECT
  UUID_TO_BIN(UUID(), true),
  requester.id,
  addressee.id,
  'PENDING',
  CURRENT_TIMESTAMP(6),
  0
FROM `user` requester
INNER JOIN `user` addressee ON addressee.username = :addressee
WHERE requester.username = :requester
