import groovy.json.JsonSlurper
// https://docs.groovy-lang.org/latest/html/gapi/groovy/json/JsonSlurper.html
// Mais rapido da categoria dos consumidores de json
import java.nio.file.Files
import java.nio.file.Paths

def inputFilePath = "./json.json"
def outputFilePath = "./output.csv"

// Fiz essa varial para escolhe qual dos cmapos do kson = 'productChild', 'productFather', ou 'reference'
def fieldToExport = "productChild"

// Aqui ele lé em formato de file os json, e consegue interpretasr os campos 
def fileContent = new File(inputFilePath).text
def jsonSlurper = new JsonSlurper()
def jsonData = jsonSlurper.parseText(fileContent)

// valida se os campo tão valido
if (!jsonData.every { it.containsKey(fieldToExport) }) {
    println "O campo '${fieldToExport}' não existe em todos os objetos do JSON. Verifique o arquivo."
    System.exit(1)
}

// faz a coleta do campo escolhido do fieltoexport setado na linha 11
def fieldValues = jsonData.collect { it[fieldToExport] }

// cria o arquivo csv com o resulktado do field 
def csvContent = new StringBuilder("SelectedField\n")
fieldValues.each { csvContent.append("${it}\n") }

// escreve no padrão utf dentro do arquivo csv, no linux vai precisar dar chmod, fiz no windws
Files.write(Paths.get(outputFilePath), csvContent.toString().getBytes("UTF-8"))

println "Arquivo CSV gerado com sucesso em: ${outputFilePath}"
