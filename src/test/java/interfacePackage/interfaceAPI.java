package interfacePackage;

import io.cucumber.datatable.DataTable;
import org.json.JSONException;

public interface interfaceAPI {
    public void getETAFare();

    public Boolean matchesETAFare(DataTable table);

    public void findBookInCUE() throws JSONException;
}
