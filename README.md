#Our HTML Project!

Hey dudes, I thought I'd throw up some git resources for y'all, so we can work on this thing. For our purposes, we should have it pretty simple. Since there's only three of us, we can probably all just work off the "master" branch. That saves us having to worry about branches or anything. 

After cloning the repository, you really only need four things:

##add
The add command works like "save". It allows you to write any changes to your local branch. You may add as often as you like, and can either add a specific file ("git add myfile.txt"), or all the files in the directory ("git add *").

##commit
Commits usually happen at the end of a work session, or after completing a new feature/method/etc. The commit is what will be pushed to the master repository when the time comes. The commit message should reflect what you added/changed, and will be visible to everyone.

##push
Pushing will send all of your commits to the master repository. If the master repository has been changed since your last pull, you will be prompted to merge the two versions.

##pull
The pull command pulls all the work on the master repository to your local machine. If you have unpushed commits, you will be prompted to push them before you pull.

If you want to read some more on all this, this guide is pretty good: https://rogerdudler.github.io/git-guide/


If you want to practice, feel free to use this file and play around with it!
