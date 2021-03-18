package com.example.flacsearcher.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flacsearcher.Interface.CostomItemClickListener
import com.example.flacsearcher.R
import com.example.flacsearcher.model.SongModel
import com.example.flacsearcher.service.PlayMusicService
import java.util.concurrent.TimeUnit

class SongListAdapter(SongModel:ArrayList<SongModel>, context: Context):RecyclerView.Adapter<SongListAdapter.SongListViewHolder>() {
    var mContext = context
    var mSongModel = SongModel
    var allMusicList: ArrayList<String> = ArrayList()
    var songName: String ?=null

    companion object{
        const val MUSICLIST = "musiclist"
        const val MUSICITEMPOS = "pos"
    }

    override fun getItemCount(): Int {
        return mSongModel.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.music_row, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        val model = mSongModel[position]
        songName = model.mSongName
        val songDuration = toMandS(model.mSongDuration.toLong())
        holder.songTV.text = songName
        holder.durationTV.text = songDuration
        holder.setOnCostomItemClickListener(object: CostomItemClickListener {
            override fun onCostomItemClick(view: View, pos: Int) {
                for (i in 0 until mSongModel.size){
                    allMusicList.add(mSongModel[i].mSongPath)
                }
                val musicDataIntent = Intent(mContext, PlayMusicService::class.java)
                musicDataIntent.putStringArrayListExtra(MUSICLIST,allMusicList)
                musicDataIntent.putExtra(MUSICITEMPOS, pos)
                mContext.startService(musicDataIntent)
            }
        })
    }

    private fun toMandS(millis: Long): String {
        return String.format(
            "%2d:%2d",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(millis)
            )
        )
    }

    class SongListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var songTV:TextView = itemView.findViewById(R.id.song_name_tv)
        var durationTV:TextView = itemView.findViewById(R.id.song_duration_tv)
        private var albumnArt:ImageView = itemView.findViewById(R.id.al_img_view)
        private var mCostomItemClickListener:CostomItemClickListener?=null
        init {
            itemView.setOnClickListener(this)
        }
        fun setOnCostomItemClickListener(costomItemClickListener:CostomItemClickListener){
            this.mCostomItemClickListener = costomItemClickListener
        }

        override fun onClick(v: View?) {
            this.mCostomItemClickListener!!.onCostomItemClick(v!!, adapterPosition)
        }
    }



















}