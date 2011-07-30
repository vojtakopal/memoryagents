source("magents_experiments.R")

generateSingleGraph("pr_silent", "PR agents without communication")
ggsave(file="pr_silent.eps")  