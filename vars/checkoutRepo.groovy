def call(String REPO_URL, String BRANCH_NAME = 'main', String credentialsId = null) {
    git url: REPO_URL, branch: BRANCH_NAME, credentialsId: credentialsId
}
