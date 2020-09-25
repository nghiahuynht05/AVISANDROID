package interfacePackage;

import io.cucumber.datatable.DataTable;
import org.json.JSONException;

public interface interfaceAPI {
    public void getETAFare();

    public Boolean matchesETAFare(DataTable table);

    public String findBookInCUE() throws JSONException;

    public String tokenAuthorization();

    public void getPaymentMethod();

    public Boolean matchesPaymentMethod(DataTable table);
}
