#!/usr/bin/env bash
for f in downloads/*.tar.xz; do
  d=`basename $f .tar.xz`
  echo f
  echo d
  mkdir raw/$d
  (cd raw/$d && tar xvf ../../$f)
done