@Grab('org.apache.poi:poi-ooxml:5.2.3')
import groovy.json.JsonSlurper
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.*

def jsonFile = new File('produtos.json')

// Carrega o arquivo JSON
def jsonSlurper = new JsonSlurper()
def data = jsonSlurper.parse(jsonFile)

// Extrai apenas as colunas desejadas para cada produto
def produtos = data.items.collect { item ->
    [
        entity_id: item.entity_id,
        sku: item.sku,
        name: item.name,
        meta_title: item.meta_title
    ]
}

// Cria uma planilha Excel com Apache POI
def workbook = new XSSFWorkbook()
def sheet = workbook.createSheet("Produtos Selecionados")

// Adiciona cabeçalhos
def headerRow = sheet.createRow(0)
def headers = ["entity_id", "sku", "name", "meta_title"]
headers.eachWithIndex { header, idx ->
    headerRow.createCell(idx).setCellValue(header)
}

// Adiciona os dados dos produtos
produtos.eachWithIndex { produto, rowIndex ->
    def row = sheet.createRow(rowIndex + 1)
    headers.eachWithIndex { header, colIndex ->
        row.createCell(colIndex).setCellValue(produto[header]?.toString() ?: "")
    }
}

// Salva o arquivo Excel
def excelFile = new File("produtos_selecionados.xlsx")
excelFile.withOutputStream { outputStream ->
    workbook.write(outputStream)
}
workbook.close()

println "Arquivo Excel gerado com sucesso: ${excelFile.absolutePath}"
