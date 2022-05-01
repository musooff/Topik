pipeline {
    triggers { issueCommentTrigger('.*test this please.*') }

    options { disableConcurrentBuilds() }

    agent any
    stages {
        stage("Clean project") {
            steps {
                sh './gradlew clean'
            }
        }

        stage('Check Testcode(Debug)') {
            steps {
                sh './gradlew testDebugUnitTest'
            }
        }

        stage('Standard Build(Debug)') {
            steps {
                sh './gradlew assembleDebug'
            }
        }

        stage('SonarQube report to PR') {
            /* environment {
                scannerHome = tool 'SonarQube Scanner'
            } */

            steps {
                reportSonarQubePullRequest()
            }
        }

        stage('SonarQube report to Dashboard') {
            when {
                branch 'develop' // Report to dashboard will be performed on develop branch only.
            }
            /* environment {
                scannerHome = tool 'SonarQube Scanner'
            } */

            steps {
                reportSonarQubeDashboard()
            }
        }

        stage('Code Inspection') {
            parallel {
                stage('Report - Lint coach') {
                    steps {
                        sh './gradlew lintDebug'
                        reportLintCoachPullRequest()
                    }
                }

                stage('Report - basic info') {
                    steps {
                        reportIssueDetailsPullRequest()
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWorkspace()
        }
    }
}

void reportSonarQubePullRequest() {
    script {
        if (evn.CHANGE_ID) {
            pullRequestId = env.CHANGE_ID
            echo "Reported to PR: ${pullRequestId}"
        }
    }
}

void reportSonarQubeDashboard() {
    script {
        if (!env.CHANGE_ID) {
            echo "Reported to Dashboard"
        }
    }
}

void reportLintCoachPullRequest() {

}

void reportIssueDetailsPullRequest() {

}

void cleanWorkspace() {
    cleanWs()
    dir("${env.WORKSPACE}@tmp") {
        deleteDir()
    }
    dir("${env.WORKSPACE}@script") {
        deleteDir()
    }
    dir("${env.WORKSPACE}@script@tmp") {
        deleteDir()
    }
}