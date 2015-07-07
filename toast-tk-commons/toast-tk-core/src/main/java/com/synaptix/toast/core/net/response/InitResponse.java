package com.synaptix.toast.core.net.response;

import java.util.ArrayList;
import java.util.List;

import com.synaptix.toast.core.net.request.IIdRequest;

/**
 * Created by skokaina on 07/11/2014.
 */
public class InitResponse implements IIdRequest {

	private String id;

	public String text;

	public List<String> items = new ArrayList<String>();

	@Override
	public String getId() {
		return id;
	}
}
