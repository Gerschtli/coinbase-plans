with import <nixpkgs> { };

mkShell {
  name = "coinbase-plans";

  buildInputs = [
    jdk15
  ];

  JAVA_HOME = "${jdk15}/lib/openjdk";
}
