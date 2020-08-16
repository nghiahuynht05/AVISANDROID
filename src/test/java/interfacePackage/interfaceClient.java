package interfacePackage;

import io.cucumber.datatable.DataTable;

public interface interfaceClient {
    public void touchPickUp(DataTable table);

    public void touchDestination(DataTable table);

    public Boolean waitingHomeScreen();

    public void touchBookType(String string);

    public Boolean requestBook();

    public void selectServiceType(String string);

    public void selectCarType(String string);

    public void getContentMsg();

    public Boolean matchResponseMsg(String string);
}
