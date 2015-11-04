package com.example.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import com.example.entity.Users;
import com.example.service.UsersService;

@Named
@SessionScoped
public class EditView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;

	@Inject
	private UsersService UsersService;

	private final static String[] brands;
	private final static int[] listId;

	static {
		brands = new String[3];
		brands[0] = "";
		brands[1] = "female";
		brands[2] = "male";
	}

	static {
		listId = new int[3];
		listId[0] = 31;
		listId[1] = 32;
		listId[2] = 33;
	}

	public int[] getListid() {
		return listId;
	}

	public List<String> getBrands() {
		return Arrays.asList(brands);
	}

	public void onCellEdit(CellEditEvent event) {

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		DataTable table = (DataTable) event.getSource();
		Users u = (Users) table.getRowData();

		System.out.println(u.getUserPhoto().getPhotoId());
		System.out.println(u.getUserPhoto().getPhotoName());

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed",
					"Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		UsersService.updateUser(u);

	}

}
