package com.kht.backend.service.model;

import lombok.Data;

@Data
public class ColumnValueModel implements Cloneable{

    private String valueCode;

    private String value;

    @Override
    public ColumnValueModel clone() {
        ColumnValueModel o = null;
        try
        {
            o = (ColumnValueModel) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return o;
    }
}
