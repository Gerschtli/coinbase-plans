with import <nixpkgs> { };

mkShell {
  name = "coinbase-plans";

  buildInputs = [
    jdk14
  ];

  JAVA_HOME = "${jdk14}/lib/openjdk";
}
