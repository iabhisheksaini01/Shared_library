def call() {
    sh 'mvn surefire-report:report-only'
}
