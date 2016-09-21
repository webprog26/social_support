package com.example.webprog26.support.interfaces;

import com.example.webprog26.support.models.FamilyClient;
import com.example.webprog26.support.models.PersonClient;

/**
 * Created by webprog26 on 06.03.2016.
 */
public interface OnItemClickListener {
    void onItemClick(FamilyClient client);
    void onItemClick(PersonClient client);
}
