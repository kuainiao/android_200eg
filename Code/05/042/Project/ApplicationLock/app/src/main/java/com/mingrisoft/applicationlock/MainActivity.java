package com.mingrisoft.applicationlock;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.mingrisoft.applicationlock.LockPatternView;
import com.mingrisoft.applicationlock.LockPatternView.Cell;
import com.mingrisoft.applicationlock.LockPatternView.OnPatternListener;

public class MainActivity extends Activity {
	private LockPatternView mLockPatternView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		
		mLockPatternView = (LockPatternView) findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(new OnPatternListener(){

			@Override
			public void onPatternStart() {
				// TODO Auto-generated method stub
				System.out.println("mLockPatternView:onPatternStart");
			}

			@Override
			public void onPatternCleared() {
				// TODO Auto-generated method stub
				System.out.println("mLockPatternView:onPatternCleared");
			}

			@Override
			public void onPatternCellAdded(List<Cell> pattern) {
				// TODO Auto-generated method stub
				System.out.println("mLockPatternView:onPatternCellAdded="+pattern.size());
				
			}

			@Override
			public void onPatternDetected(List<Cell> pattern) {
				// TODO Auto-generated method stub
				System.out.println("mLockPatternView:onPatternDetected");
				StringBuffer sb = new StringBuffer("输入的密码：");
				for(Cell cell : pattern){
					sb.append("("+cell.getRow()+","+cell.getColumn()+"),");
				}
				sb.deleteCharAt(sb.length()-1);
				Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();;
				
			}
			
		});
		this.findViewById(R.id.clear).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mLockPatternView.clearPattern();
			}
			
		});
	}

}
