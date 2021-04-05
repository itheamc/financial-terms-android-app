package com.itheamc.financialterms.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Terms {
    private int _id;
    private String _title;
    private String _body;
    private final String _author = "Amit Chaudhary";

    // Constructor
    public Terms() {
    }

    // Constructor with parameters
    public Terms(int _id, String _title, String _body) {
        this._id = _id;
        this._title = _title;
        this._body = _body;
    }


    // Getters

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_body() {
        return _body;
    }

    public void set_body(String _body) {
        this._body = _body;
    }

    public String get_author() {
        return _author;
    }

    // Overriding toString() method for debugging purpose only
    @Override
    public String toString() {
        return "Terms{" +
                "_id='" + _id + '\'' +
                ", _title='" + _title + '\'' +
                ", _body='" + _body + '\'' +
                ", _author='" + _author + '\'' +
                '}';
    }

    // Overriding equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terms terms = (Terms) o;
        return get_id() == terms.get_id() &&
                get_title().equals(terms.get_title()) &&
                get_body().equals(terms.get_body()) &&
                Objects.equals(get_author(), terms.get_author());
    }


    // Creating DiffUtils object
    public static DiffUtil.ItemCallback<Terms> termsItemCallback = new DiffUtil.ItemCallback<Terms>() {
        @Override
        public boolean areItemsTheSame(@NonNull Terms oldItem, @NonNull Terms newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Terms oldItem, @NonNull Terms newItem) {
            return oldItem.get_id() == newItem.get_id() &&
                    oldItem.get_title().equals(newItem.get_title()) &&
                    oldItem.get_body().equals(newItem.get_body()) &&
                    oldItem.get_author().equals(newItem.get_author());
        }
    };

}
