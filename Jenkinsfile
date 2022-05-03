import groovy.json.JsonSlurper

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
            steps {
                reportSonarQubePullRequest()
                addSonarQubeReviewComments()
            }
        }

        stage('SonarQube report to Dashboard') {
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
        if (env.CHANGE_ID) {
            pullRequestId = env.CHANGE_ID
            sh './gradlew sonarqube -Dsonar.github.pullRequest=$pullRequestId'
        }
    }
}

void addSonarQubeReviewComments() {
    def response = httpRequest authentication: 'sonarqubeCredentials', url: "https://fb41-221-141-140-219.jp.ngrok.io/api/issues/search?ps=10&componentKeys=musooff_Topik_AYB-ZFlS8-xhCYxQvxYf"
    def jsonSlurper = new JsonSlurper()
    def root = jsonSlurper.parseText(response.content)

    // Comment new issues
    root.issues.each { issue ->
        def path = issue.component.split(":")[1]
        def line = issue.line
        def message = issue.message
        if (!path || !line || !message) return
        def key = issue.key
        def issueLink = "[Details](https://fb41-221-141-140-219.jp.ngrok.io/project/issues?id=musooff_Topik_AYB-ZFlS8-xhCYxQvxYf&issues=${key}&open=${key})"
        def body = "${message} ${issueLink}"
        for (comment in pullRequest.reviewComments) {
            if (comment.body == body) return
        }
        pullRequest.reviewComment(env.GIT_PREVIOUS_COMMIT, path, line, body)
    }

    // Remove resolved comments
    /* pullRequest.reviewComments.each { reviewComment ->
        if (reviewComment.body !==~ /.*\[Details\]\(.*\)/) return
    } */
}

void reportSonarQubeDashboard() {
    script {
        if (!env.CHANGE_ID) {
            sh './gradlew sonarqube'
        }
    }
}

void reportLintCoachPullRequest() {

}

void reportIssueDetailsPullRequest() {
    script {
        //maybeWriteBtsComment()
        maybeWriteGitCommandComment()
    }
}

void maybeWriteGitCommandComment() {
    echo "Git command assistor : Check and write git command comment"
    if (env.CHANGE_ID) {
        def gitCommandCommentPrefix = "Git hint"
        def prId = env.CHANGE_ID
        def gitCommandCommentBody = gitCommandCommentPrefix + " : 변경사항을 로컬에서 확인하고 싶으면 다음 명령어를 활용해 주세요 " +
                "\r\n`\$ git fetch origin pull/" +
                prId +
                "/head:pr/" +
                prId +
                " && git checkout pr/" +
                prId +
                "`"
        alreadyWrittenCommentId = getCommentIdForAlreadyExist(gitCommandCommentPrefix)
        if (alreadyWrittenCommentId != null) {
            echo "Git hint : Comment edit : $alreadyWrittenCommentId :: $gitCommandCommentBody"
            pullRequest.editComment(alreadyWrittenCommentId, gitCommandCommentBody)
        } else {
            echo "Git hint : Comment write : $gitCommandCommentBody"
            pullRequest.comment(gitCommandCommentBody)
        }
    } else {
        echo "Pass - Git command comment."
    }
}

/**
 * 특정 문자열이 포함된 comment를 찾아서 반환한다.
 **/
def getCommentIdForAlreadyExist(String checkContainsValue) {
    for (comment in pullRequest.comments) {
        String commentBody = comment.body
        if (commentBody.contains(checkContainsValue)) {
            return comment.id
        }
    }
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