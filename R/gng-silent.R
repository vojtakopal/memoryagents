source("magents_experiments.R")

generateSingleGraph("gng_silent", "GNG agents without communication")
ggsave(file="gng_silent.eps")