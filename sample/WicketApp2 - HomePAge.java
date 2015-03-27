/*
 * HomePage.java
 *
 * Created on 2015/03/01, 4:42
 */
package com.myapp.wicket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

public class HomePage extends BasePage {

    public HomePage() {
//        add(new Label("message", "Hello, World!"));

        List<String> headerList = new ArrayList<String>();
        headerList.add("C1");
        headerList.add("C2");
        headerList.add("C3");

        ListView parentHeaderView = new ListView("titleList", headerList) {

            @Override
            protected void populateItem(ListItem li) {
                String title = (String) li.getModelObject();
                Label titleLabel = new Label("title", title);
                li.add(titleLabel);
            }
        };
        add(parentHeaderView);

        // ? テーブルを作成する為のListを作成
        List<List> parentList = new ArrayList<List>();
// ? 行に当たる部分→このListの要素数で行数が決まる
        List<String> childList1 = new ArrayList<String>();
// ? 列に当たる部分→このListの要素数で列数が決まる
        childList1.add("childList1-1");
        childList1.add("childList1-2");
        childList1.add("childList1-3");

        List<String> childList2 = new ArrayList<String>();
        childList2.add("childList2-1");
        childList2.add("childList2-2");
        childList2.add("childList2-3");
        List<String> childList3 = new ArrayList<String>();
        childList3.add("childList3-1");
        childList3.add("childList3-2");
        childList3.add("childList3-3");
        parentList.add(childList1);
        parentList.add(childList2);
        parentList.add(childList3);

// ? <tr>に対応するListView
        ListView parentListView = new ListView("parentListView", parentList) {
            @Override
            protected void populateItem(ListItem parentItem) {
                // ? ?の要素を取得する
                List<String> childList = (List<String>) parentItem.getModelObject();
                // ? <td>に対応するListView
                ListView childListView = new ListView("childListView", childList) {
                    @Override
                    protected void populateItem(ListItem childItem) {

//                        Label divLabel = new Label("divLabel", "<span>"+childItem.getModelObject()+"</span>");
                        Label divLabel = new Label("divLabel", "<input type=\"text\" value=\""+childItem.getModelObject()+"\"></input>");
//                        Label divLabel = new Label("divLabel", "<input type=\"text\" ></input>");
                        divLabel.setEscapeModelStrings(false);
                        childItem.add(divLabel);
                        
                        // ? ?の要素を取得する
                        String labelStr = (String) childItem.getModelObject();
//                        Label listlabel = new Label("listlabel", labelStr);
                        TextField listlabel = new TextField("listlabel", new Model(labelStr));
//                        List SITES = Arrays.asList(new String[] { "The Server Side", "Java Lobby", "Java.Net" });
//                       DropDownChoice listlabel = new DropDownChoice("listlabel", SITES);
                        // ? <td>の子要素にラベルを追加
//                        childItem.add(listlabel);
                    }
                };
                // ? <tr>の子要素に<td>に対応するListViewを追加
                parentItem.add(childListView);
            }
        };
// 生成したListViewをページに追加
        add(parentListView);
    }

}
