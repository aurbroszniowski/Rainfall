Workspace Layout
================

This repository is the public parent/workspace repository for Rainfall.
It is useful as a lightweight entry point for side-by-side development, but the
runtime modules live in their own repositories.

Which repo should I clone?
--------------------------

- Clone `Rainfall` first if you want the full public maintainer workspace.
- Clone `Rainfall-core` directly if you only want to work on the framework
  core.

Recommended local layout
------------------------

Clone the public repositories as siblings in one workspace directory:

```text
workspace/
  Rainfall/
  Rainfall-core/
  Rainfall-ehcache/
  Rainfall-store/
```

That layout keeps each Git history independent while still making it easy to
work across repositories locally.

Bootstrap
---------

From a fresh clone of `Rainfall`, run:

```bash
sh scripts/bootstrap-workspace.sh
```

The script clones the public sibling repositories next to `Rainfall` if they
are not already present.

After bootstrap, day-to-day framework development happens in `Rainfall-core`.
The outer `Rainfall` repository remains the lightweight workspace shell and
bootstrap entry point.

Notes
-----

- `Rainfall-core` is the main framework repository.
- `Rainfall-ehcache` and `Rainfall-store` are optional sibling repositories
  commonly used during local development.
- Private/internal benchmarking or harness tooling is intentionally not part of
  this public workspace bootstrap.
