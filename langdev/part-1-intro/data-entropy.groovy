def vals = [ 0x31, 0x20, 0x2b, 0x20, 0x32, 0x2e, 0x35, 0x20, 0x2a, 0x20, 
0x61, 0x20, 0x2d, 0x20, 0x34, 0x20, 0x5e, 0x20, 0x62, 0x28, 0x20, 0x35, 0x20, 0x29, 0x0a ]

//println vals.unique().size()
println "bytes entropy:" + entropy( vals )

def svals = [ '1',' ','+',' ','2.5',' ','*',' ','a',' ','-',' ','4',' ','^',' ','b','(',' ','5',' ',')' ]
println "lexems entropy:" + entropy( svals )

def fsvals = svals.findAll { it.trim().length()>0 }
println fsvals
println "filtered lexems entropy:" + entropy( fsvals )