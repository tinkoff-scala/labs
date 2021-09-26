FROM mozilla/sbt
COPY . /files
ENTRYPOINT ["/files"]
