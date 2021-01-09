package com.xter.foldablerecyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xter.foldablerecyclerview.adapter.FoldableRecyclerViewAdapter;
import com.xter.foldablerecyclerview.adapter.FoldableRecyclerViewAdapter.Unit;
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
		final DirsAdapter adapter = new DirsAdapter(this,R.layout.item_group,R.layout.item_child,data);

		rvFolder.setAdapter(adapter);
		adapter.setOnItemClickLitener(new FoldableRecyclerViewAdapter.OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {
				Object o = adapter.getItem(position);
				System.out.println(o);
			}

			@Override
			public void onItemLongClick(View view, int position) {
				adapter.itemRemove(position);
			}
		});

	}

	private List<FoldableRecyclerViewAdapter.Unit<Directory, File>> getData(){
		List<FoldableRecyclerViewAdapter.Unit<Directory, File>> dirs = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			List<File> fileList = new ArrayList<>();
			for (int j = 0; j < i; j++) {
				File file = new File("file"+j,j*100);
				fileList.add(file);
			}
			Directory directory = new Directory("dir"+i,fileList.size());

			FoldableRecyclerViewAdapter.Unit<Directory,File> unit = new FoldableRecyclerViewAdapter.Unit<>(directory,fileList);
			dirs.add(unit);
		}

		return dirs;
	}

}
