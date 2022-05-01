import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class conversion {

	public static void main(String[] args) throws Exception 
	{
    	
		File jsonFile = new File("./src/main/resources/jsonFile.json").getAbsoluteFile();

		// create instance of the ObjectMapper class to map JSON data  
		ObjectMapper mapper = new ObjectMapper();
		// enable pretty printing
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		// read map from file
		MapType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		Map<String, Object> map = mapper.readValue(jsonFile, mapType);

		// generate pretty JSON from map
		String json = mapper.writeValueAsString(map);
		// split by system new lines
		String[] strings = json.split(System.lineSeparator());
        
		//Create a Document object.
		PDDocument pdDocument = new PDDocument();
		
		// Create a Page object
		PDPage pdPage = new PDPage();
		
		// Add the page to the document and save the document to a desired file.
		pdDocument.addPage(pdPage);
		
		try {
			// Create a Content Stream
			PDPageContentStream pdPageContentStream = new PDPageContentStream(pdDocument, pdPage);

			// Start the stream
			pdPageContentStream.beginText();
			
			// Setting the leading
			pdPageContentStream.setLeading(14.5f);

			// Set the X and Y corodinates for the text to be positioned
			pdPageContentStream.newLineAtOffset(25, 725);

			// Set a Font and its Size
			// We cannot use the standard fonts provided.
			PDFont unicodeFont = PDType0Font.load(pdDocument, new File("/home/shivani/workspace/Json_To_Pdf/src/main/resources/ARIALUNI.TTF"));	
			pdPageContentStream.setFont(unicodeFont, 14);
			
			for (String string : strings) 
			{ 
				pdPageContentStream.showText(string); 
				pdPageContentStream.newLine(); 
			}
			
			// End the Stream
			pdPageContentStream.endText();
			
			// Once all the content is written, close the stream
			pdPageContentStream.close();

			pdDocument.save("/home/shivani/workspace/Json_To_Pdf/src/main/resources/employeeDetails.pdf");
			pdDocument.close();
			
			System.out.println("PDF saved to the location !!!");

		} catch (IOException ioe) {
			System.out.println("Error while saving pdf" + ioe.getMessage());
		}

	}
}
