#.//R//gng_grid.txt
#.//R//gng.txt
library(ggplot2)
library(quantreg)

kilo_fmt <- function(x, ...) {
     val<-as.numeric(x)
     paste(round(val / 1000, digits=1), "k", sep = "")
}

computeMaxHunger<-function(data){
	data$max<-apply(data,1,function(row) round(as.numeric(max(row[4:9])),digits=2))
	data
}

createAgentKindLabels<-function(data){
	data$titles <- ordered(data$kind,levels = c('gng', 'grid', 'pr', 'random'),labels = c('GNG agents', 'Grid agents', 'Pure reactive agents', 'Random agents'))
	data
}

loadData<-function(name){
     path<-paste(".//agents//",name,".txt",sep="")
     read.table(path,sep=";",header=TRUE,comment.char="#")
}

showStats<-function(name,dataWithMax){
	paste(name,	"median",median(dataWithMax),
		     	"mean",mean(dataWithMax),
		    	"min",min(dataWithMax),
   			"max",max(dataWithMax))
}

generateOneAgent<-function(name){

	sim<-loadData(name)
	sim<-computeMaxHunger(sim)

	sim<-sim[sim$id=="0",]

#	sim<-sim[1:1000,]

	print(statSample(name,sim[17000:100000,]$max))

	ggplot(sim,aes(step))+
		geom_line(aes(y=food_0),colour="red", size=1)+
		geom_line(aes(y=food_1),colour="blue", size=1)+
		geom_line(aes(y=food_2),colour="cyan", size=1)+
		geom_line(aes(y=food_3),colour="green", size=1)+
		geom_line(aes(y=food_4),colour="magenta", size=1)+
		geom_line(aes(y=food_5),colour="orange", size=1)+
		scale_y_continuous(expand=c(0,0))+
		scale_x_continuous(expand=c(0,0))+
		xlab("Simulation step")+
		ylab("Hunger")
		
	

}

generateSingleGraph<-function(name,headline){

	sim<-loadData(name)
	sim<-computeMaxHunger(sim)
	sim<-createAgentKindLabels(sim)

	statSample<-sim[17000:100000,]
	print(showStats(name,statSample$max))
	
	theme_get()

	none <- theme_blank()
	p<-ggplot(sim, aes(x=step,y=max,colour=id,group=id))+
		geom_line(size=1, colour="lightgrey")+
#		geom_smooth()+
#		stat_quantile()+
		stat_smooth()+
		scale_y_continuous(limits=c(0,1),expand=c(0,0))+
		scale_x_continuous(expand=c(0,0),formatter = kilo_fmt)+
#		opts(legend.position = "none")+
		opts(panel.background = none)+
		xlab("Simulation steps")+
		ylab("Maximal hunger")+
		opts(title = headline)+
		opts(plot.title=theme_text(size=20))+
		opts(axis.text.x=theme_text(size=14))+
		opts(axis.text.y=theme_text(size=14))+
		opts()

	p

}

generateCompareGraph<-function(name){

	sim<-loadData(name)
	sim<-computeMaxHunger(sim)
	sim<-createAgentKindLabels(sim)

	statSample<-sim[17000:100000,]

	print(showStats(paste(name,"all"),statSample$max))

	for (i in unique(sim$kind)) {
		print(showStats(paste(name,i),statSample[statSample$kind==i,]$max))
	}

	none <- theme_blank()
	p<-ggplot(sim, aes(x=step,y=max,colour=titles,group=id))+
		geom_line(colour="lightgrey")+
		geom_smooth()+
		scale_y_continuous(expand=c(0,0))+
		scale_x_continuous(expand=c(0,0),limits=c(1000,5000),formatter = kilo_fmt)+
#		opts(legend.position = "none")+
		opts(panel.background = none)+
		xlab("Simulation steps")+
		ylab("Maximal hunger")

	p
	
}

generateOverallGraphs<-function(name, headline){
	
	sim<-loadData(name)
	sim<-computeMaxHunger(sim)
	sim<-createAgentKindLabels(sim)
	
	theme_get()

	statSample<-sim[17000:100000,]

	print(showStats(paste(name,"all"),statSample$max))

	for (i in unique(sim$kind)) {
		print(showStats(paste(name,i),statSample[statSample$kind==i,]$max))
	}

	none <- theme_blank()
	p<-ggplot(sim, aes(x=step,y=max,colour=titles,group=id))+
		geom_line(size=1, colour="lightgrey")+
#		geom_smooth()+
#		stat_quantile()+
		stat_smooth()+
		scale_y_continuous(limits=c(0,1),expand=c(0,0))+
		scale_x_continuous(expand=c(0,0),formatter = kilo_fmt)+
		opts(legend.position = "none")+
		opts(panel.background = none)+
		facet_wrap(~titles, ncol=1)+
		xlab("Simulation steps")+
		ylab("Maximal hunger")+
		opts(title = headline)+
		opts(plot.title=theme_text(size=20))+
		opts(axis.text.x=theme_text(size=14))+
		opts(axis.text.y=theme_text(size=14))+
		opts()

	p

}

#generateOverallGraphs("sample", "GNG")

##
## Process:
##

#generateSingleGraph("sample", "Sample")
#ggsave(file=".//R//agents//gng.pdf")

#generateSingleGraph("grid", "Grid agents with communication")
#ggsave(file=".//R//agents//grid.pdf")

#generateSingleGraph("random", "Random agents with communication")
#ggsave(file=".//R//agents//random.pdf")

#generateSingleGraph("pr", "PR agents with communication")
#ggsave(file=".//R//agents//pr.pdf")

#generateSingleGraph("gng_silent", "GNG agents without communication")
#ggsave(file=".//R//agents//gng_silent.pdf")

#generateSingleGraph("grid_silent", "Grid agents without communication")
#ggsave(file=".//R//agents//grid_silent.pdf")

#generateSingleGraph("random_silent", "Random agents without communication")
#ggsave(file=".//R//agents//random_silent.pdf")

#generateSingleGraph("pr_silent", "PR agents without communication")
#ggsave(file=".//R//agents//pr_silent.pdf")

#window()
#generateOverallGraphs("gng", "GNG agents with communication")
#ggsave(file=".//R//agents//gng_comm.pdf")


#generateCompareGraph("gng-grid-pr-random")
#generateOverallGraphs("sample")
#generateGraphs("gng")


#tests
#dat <- data.frame(id=rep(1:50,each=15), 
#group2=rep(rep(1:5,each=10),each=15), time=rep(1:15,50), value = 
#rnorm(50*15), col = factor(sample(1:4,15*50,replace=TRUE))) 

#p <- ggplot(dat,aes(x=time,y=value,group=id,color=col)) + 
#geom_line(alpha = 0.6)  +facet_grid(.~group2) 
#print(p) 
