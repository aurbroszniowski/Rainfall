#!/bin/sh

set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPO_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/.." && pwd)
WORKSPACE_ROOT=$(dirname "$REPO_ROOT")

clone_if_missing() {
  repo_name=$1
  repo_url=$2
  repo_path="$WORKSPACE_ROOT/$repo_name"

  if [ -d "$repo_path/.git" ]; then
    echo "Already present: $repo_name"
    return 0
  fi

  echo "Cloning $repo_name into $repo_path"
  git clone "$repo_url" "$repo_path"
}

clone_if_missing "Rainfall-core" "git@github.com:aurbroszniowski/Rainfall-core.git"
clone_if_missing "Rainfall-ehcache" "git@github.com:aurbroszniowski/Rainfall-ehcache.git"
clone_if_missing "Rainfall-store" "git@github.com:aurbroszniowski/Rainfall-store.git"

echo
echo "Workspace ready under $WORKSPACE_ROOT"
