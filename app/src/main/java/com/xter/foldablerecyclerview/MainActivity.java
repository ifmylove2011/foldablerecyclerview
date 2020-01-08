package com.xter.foldablerecyclerview;

import android.app.Activity;
import android.os.Bundle;

import com.xter.foldablerecyclerview.adapter.FoldableRecyclerViewAdapter;
import com.xter.foldablerecyclerview.adapter.QuickItemDecoration;
import com.xter.foldablerecyclerview.entity.Directory;
import com.xter.foldablerecyclerview.entity.File;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends Activity {

	private RecyclerView rvFolder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView(){
		rvFolder = findViewById(R.id.rv_folder);

		rvFolder.addItemDecoration(new QuickItemDecoration(this, QuickItemDecoration.VERTICAL_LIST));
		rvFolder.setLayoutManager(new LinearLayoutManager(this));

		List<FoldableRecyclerViewAdapter.Unit<Directory, File>> data = getData();
		DirsAdapter adapter = new DirsAdapter(this,R.layout.item_group,R.layout.item_child,data);

		rvFolder.setAdapter(adapter);
	}

	private List<FoldableRecyclerViewAdapter.Unit<Directory, File>> getData(){
		List<FoldableRecyclerViewAdapter.Unit<Directory, File>> dirs = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			Directory directory = new Directory("dir"+i,i*10);
			List<File> fileList = new ArrayList<>();
			for (int j = 0; j < i; j++) {
				File file = new File("file"+j,j*100);
				fileList.add(file);
			}

			FoldableRecyclerViewAdapter.Unit<Directory,File> unit = new FoldableRecyclerViewAdapter.Unit<>(directory,fileList);
			dirs.add(unit);
		}

		return dirs;
	}

}
