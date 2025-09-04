def call() {
    junit '**/target/surefire-reports/*.xml'
    archiveArtifacts artifacts: 'target/**/*.jar, target/surefire-reports/*.*', fingerprint: true
}
