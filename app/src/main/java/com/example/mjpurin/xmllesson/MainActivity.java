package com.example.mjpurin.xmllesson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Person> list;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyTask task=new MyTask();
        task.execute("http://10.0.2.2/addressbook.xml");
    }

    public void xmlParser(String data){
        list=new ArrayList<Person>();
        XmlPullParser parser= Xml.newPullParser();
        Person p=null;
        try {
            parser.setInput(new StringReader(data));
            int eventType=parser.getEventType();
            while(eventType !=XmlPullParser.END_DOCUMENT){
                if(eventType ==XmlPullParser.START_TAG){
                    if(parser.getName().equals("person")){
                        p=new Person();
                    }else if(parser.getName().equals("name")){
                        p.name=parser.nextText();
                    }else if(parser.getName().equals("email")){
                        p.email=parser.nextText();
                    }
                }
                if(eventType==XmlPullParser.END_TAG && parser.getName().equals("person")){
                    list.add(p);
                }
                eventType=parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }


    }

    class MyTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            HttpManager hm=new HttpManager();

            return hm.reqText(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            xmlParser(s);
            lv=(ListView)findViewById(R.id.listView);
            ArrayAdapter<String> adapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,list);
            lv.setAdapter(adapter);

        }
    }
}
