import goslate
 
text = "Hello World"
 
gs = goslate.Goslate()
translatedText = gs.translate(text,'fr')
 
print(translatedText)