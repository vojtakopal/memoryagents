source("magents_experiments.R")

generateSingleGraph("random_silent", "Random agents without communication")
ggsave(file="random_silent.eps")