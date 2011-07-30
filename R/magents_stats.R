loadData<-function(name){
     path<-paste(".//R//agents//",name,".txt",sep="")
     read.table(path,sep=";",header=TRUE,comment.char="#")
}

computeMaxHunger<-function(data){
	data$max<-apply(data,1,function(row) round(as.numeric(max(row[4:9])),digits=2))
	data
}

#data<-loadData("gng")

#data<-computeMaxHunger(data)

#plot(data[0:17000,]$max)

#d<-data[17000:100000,]$max

mean(d)
median(d)
min(d)
max(d)

