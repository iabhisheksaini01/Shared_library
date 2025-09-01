def installZap(String version = "2.16.1") {
    sh """
        if ls | grep -q "ZAP_"; then
            echo "ZAP already present, skipping download."
        else
            echo "Downloading ZAP ${version}..."
            wget https://github.com/zaproxy/zaproxy/releases/download/v${version}/ZAP_${version}_Linux.tar.gz
            tar -xf ZAP_${version}_Linux.tar.gz
        fi
    """
}

def runZapScan(String targetUrl, String reportName = "results.html", int port = 8082) {
    sh """
        ZAP_DIR=\$(ls -d ZAP_* | head -n 1)
        echo "Using \$ZAP_DIR"

        \${WORKSPACE}/\$ZAP_DIR/zap.sh \
            -cmd -port ${port} \
            -quickurl ${targetUrl} \
            -quickout \${WORKSPACE}/${reportName}
    """
}

def archiveReport(String reportName = "results.html") {
    archiveArtifacts artifacts: reportName
}

def sendMail(boolean success, String reportName = "results.html") {
    if (success) {
        emailext(
            to: 'sainiabhishek619@gmail.com',
            subject: "ZAP Scan SUCCESS: ${currentBuild.fullDisplayName}",
            body: """Hello Abhishek,  

The ZAP scan has completed successfully.  
HTML report is attached for details.  
""",
            attachmentsPattern: reportName
        )
    } else {
        emailext(
            to: 'sainiabhishek619@gmail.com',
            subject: "ZAP Scan FAILED: ${currentBuild.fullDisplayName}",
            body: """Hello Abhishek,  

The ZAP scan has failed.  
Please check the Jenkins console log for more details.  
"""
        )
    }
}
