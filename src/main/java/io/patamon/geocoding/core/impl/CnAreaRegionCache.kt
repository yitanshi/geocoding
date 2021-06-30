package io.patamon.geocoding.core.impl

import com.google.gson.Gson
import io.patamon.geocoding.core.RegionCache
import io.patamon.geocoding.model.RegionEntity
import io.patamon.geocoding.model.RegionType
import java.io.ByteArrayInputStream
import java.util.*
import java.util.zip.GZIPInputStream

/**
 * @author  yunpeng.gu
 * @date  2021/6/7 11:04
 * @Email:yunpeng.gu@percent.cn
 */
open class CnAreaRegionCache : RegionCache {

    private var regions: RegionEntity? = null
    private val REGION_CACHE = hashMapOf<Long, RegionEntity>()

    init {
        // 加载区域数据
        if (regions == null) {
            // TODO 从数据库中加载首层（）
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