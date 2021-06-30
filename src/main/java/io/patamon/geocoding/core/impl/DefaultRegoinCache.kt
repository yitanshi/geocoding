package io.patamon.geocoding.core.impl

import cn.hutool.core.io.FileUtil
import com.google.gson.Gson
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility.getBytes
import io.patamon.geocoding.core.RegionCache
import io.patamon.geocoding.model.RegionEntity
import io.patamon.geocoding.model.RegionType
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream




/**
 * Desc: 默认 [RegionEntity] 获取的缓存类
 *     默认从 region.dat 中获取
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2017/1/12
 */
open class DefaultRegoinCache : RegionCache {

    private var regions: RegionEntity? = null
    private val REGION_CACHE = hashMapOf<Long, RegionEntity>()

    init {
        // 加载区域数据
        if (regions == null) {
            var decode_regions = String(this.javaClass.classLoader.getResourceAsStream("core/decode_regions.json").readBytes())
//            FileUtil.writeString(decode_regions, "data/decode_regions.json", Charset.defaultCharset())
            regions = Gson().fromJson(decode_regions, RegionEntity::class.java)
        }
        // 加载cache
        REGION_CACHE.put(regions!!.id, regions!!)
        loadChildrenInCache(regions)
    }

    private fun loadChildrenInCache(parent: RegionEntity?) {
        // 已经到最底层，结束
        if (parent == null || parent.type == RegionType.Street ||
                parent.type == RegionType.Village ||
                parent.type == RegionType.PlatformL4 ||
                parent.type == RegionType.Town) return

        // 递归children
        parent.children?.forEach {
            REGION_CACHE.put(it.id, it)
            this.loadChildrenInCache(it)
        }
    }

    /**
     * 解压缩数据
     */
    private fun decode(dat: String): String {
        return String(GZIPInputStream(ByteArrayInputStream(Base64.getMimeDecoder().decode(dat))).readBytes())
    }

    private fun encode(dat: String): String{
        val out = ByteArrayOutputStream()
        val gzip = GZIPOutputStream(out)
        gzip.write(dat.toByteArray())
        gzip.flush()
        gzip.close()
        return out.toString()
    }

    /**
     * 加载全部区域列表，按照行政区域划分构建树状结构关系
     */
    override fun get(): RegionEntity {
        if (regions == null) throw IllegalArgumentException("行政规划区域数据加载失败!")
        return regions!!
    }

    /**
     * 加载区域map结构, key是区域id, 值是区域实体
     */
    override fun getCache(): Map<Long, RegionEntity> {
        return REGION_CACHE
    }

    /**
     * 新增一个region信息
     */
    override fun addRegionEntity(entity: RegionEntity) {
        this.loadChildrenInCache(entity)
        this.REGION_CACHE[entity.id] = entity
        this.REGION_CACHE[entity.parentId]?.children?.add(entity)
    }
}