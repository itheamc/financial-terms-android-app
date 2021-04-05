package com.itheamc.financialterms.viewmodel;

import androidx.lifecycle.ViewModel;

import com.itheamc.financialterms.models.Terms;

import java.util.ArrayList;
import java.util.List;

public class DataModel extends ViewModel {
    private List<Terms> termsList;
    private List<Terms> filteredList;
    private static DataModel dataModel;

    // Has not still used but created
    // Instance of the DataModel
    public static synchronized DataModel getInstance() {
        if (dataModel == null) {
            dataModel = new DataModel();
            return dataModel;
        }

        return dataModel;
    }

    // Getter and Setter
    public List<Terms> getTermsList() {
        return termsList;
    }

    public void setTermsList(List<Terms> termsList) {
        if (this.termsList == null) {
            this.termsList = new ArrayList<>();
        }
        this.termsList = termsList;
    }

    // Getter and Setter for filteredList

    public List<Terms> getFilteredList() {
        return filteredList;
    }

    public void setFilteredList(List<Terms> filteredList) {
        if (this.filteredList == null) {
            this.filteredList = new ArrayList<>();
        }
        this.filteredList = filteredList;
    }
}
