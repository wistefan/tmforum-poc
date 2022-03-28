# TMForum API

## Obeservations

- referential integrity checks and inline entity creations lead to (potentially) a lot of requests per single request -> everything has to be non-blocking and parallelized  to not timeout
- caching is very important for all the same reasons


## Open points:

- Implement patch-operations
- Assert referential integrity on deletion
- proper handling for *RefOrValue objects
- automatic resolution of relationships on get
- CanisMajor integration on patch/delete
- forwarding of CanisMajor headers
- transaction error handling(e.g. clean up if one of the calls fails)
- TESTING

