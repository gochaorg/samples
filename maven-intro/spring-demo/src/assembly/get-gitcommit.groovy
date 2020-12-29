import org.eclipse.jgit.lib.*

try {

    // find .git dir
    File dir = new File(project.basedir.toString()).getAbsoluteFile().getCanonicalFile()
    File gitdir = null
    while (true) {
        if (dir.name == '.git') {
            gitdir = dir
            break
        }
        File tdir = new File(dir, '.git')
        if (tdir.exists() && tdir.isDirectory()) {
            gitdir = tdir
            break
        }
        if (dir.getParentFile() == null) break
        dir = dir.getParentFile()
    }

    if (gitdir == null) {
        println "git dir not found"
        return;
    }

    // read git info
    def repo = new RepositoryBuilder().setGitDir(gitdir).build()

    // write commit id, if branch found
    if (repo.fullBranch) {
        println "branch $repo.fullBranch"
        def ref = repo.findRef(repo.fullBranch)
        if (ref != null) {
            def cmtId = new StringWriter()
            ref.objectId.copyTo(cmtId)
            println "commit $cmtId"

            def commitTxt = new File(project.basedir, 'target/git-commit.txt')
            commitTxt.setText("""|branch $repo.fullBranch
                             |commit $cmtId""".stripMargin())
        }
    }

} catch ( Throwable err ){
    println err
}