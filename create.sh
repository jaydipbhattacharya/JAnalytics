#!/bin/sh
echo "# JAnalytics" >> README.md 
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/jaydipbhattacharya/JAnalytics.git
git push -u origin master

