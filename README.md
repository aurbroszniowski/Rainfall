Rainfall
========

Parent of all Rainfall modules.
Rainfall is an extensible java framework to implement custom DSL based stress and performance tests.
See modules :

[Rainfall-core](https://github.com/aurbroszniowski/Rainfall-core) is the core library containing the key elements of the framework.

[Rainfall-web](https://github.com/aurbroszniowski/Rainfall-web) is the Web Application performance testing implementation.

[Rainfall-jcache](https://github.com/aurbroszniowski/Rainfall-jcache) is the JSR107 caches performance testing implementation.

[Rainfall-ehcache](https://github.com/aurbroszniowski/Rainfall-ehcache) is the Ehcache 2.x/3.x performance testing implementation.

[Rainfall-cassandra](https://github.com/aurbroszniowski/Rainfall-cassandra) for Cassandra

[Rainfall-redis](https://github.com/aurbroszniowski/Rainfall-redis) for Redis

Workspace bootstrap
-------------------

This repository can be used as a lightweight parent/workspace checkout for
side-by-side development of the public Rainfall repositories.

New developer setup:

1. Clone `Rainfall` if you want the full maintainer workspace.
2. Run `sh scripts/bootstrap-workspace.sh`.
3. Open and work in `Rainfall-core` for framework development.

If you only want to work on the framework core, you can clone
`Rainfall-core` directly and skip this workspace repo.

See [docs/workspace-layout.md](docs/workspace-layout.md) for the recommended
layout, then run:

```bash
sh scripts/bootstrap-workspace.sh
```

Thanks to the following companies for their support to FOSS:
------------------------------------------------------------

[Sonatype for Nexus](http://www.sonatype.org/)

and of course [Github](https://github.com/) for hosting this project.
