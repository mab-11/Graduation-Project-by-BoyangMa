package com.pxw.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pxw.mapper.CclassesMapper;
import com.pxw.mapper.ClassesMapper;
import com.pxw.pojo.Cclasses;
import com.pxw.pojo.Classes;
import com.pxw.service.CclassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CclassesServiceImpl implements CclassesService {

    @Autowired
    private CclassesMapper cclassesMapper;

    @Override
    public String selectByCondition(Cclasses cclasses, Integer currentPage, Integer pageSize) {
        // Pagination, query by condition
        PageHelper.startPage(currentPage, pageSize);
        List<Cclasses> Cclassess = cclassesMapper.selectByCondition(cclasses);
        // Pagination info
        PageInfo<Cclasses> ClassesPageInfo = new PageInfo<>(Cclassess);

        return JSON.toJSONString(ClassesPageInfo);
    }


    /**
     * @param oldCclasses
     * @return
     */
    @Override
    public String update(Cclasses oldCclasses) {
        // Judge whether has been modified
        Cclasses cclasses1 = cclassesMapper.selectById(oldCclasses.getId());
        // Already modified
        if (! cclasses1.equals(oldCclasses)){
            // Judge whether already exists
            List<Cclasses> cclasses = cclassesMapper.selectByName(oldCclasses.getClassesName());
            for(Cclasses cclassesSon : cclasses){
                if (oldCclasses.equals(cclassesSon)){
                    return "fail";
                }
            }
        }
        cclassesMapper.update(oldCclasses);
        return "success";
    }

    @Override
    public void deleteByIds(int[] ids) {
        cclassesMapper.deleteByIds(ids);
    }

    @Override
    public String add(Cclasses newClasses) {
        // Remove last comma
        String classesName = newClasses.getClassesName();
        String substring = classesName.substring(0, classesName.length() - 1);
        newClasses.setClassesName(substring);
        // Judge whether already exists
        List<Cclasses> cclasses = cclassesMapper.selectByCondition(newClasses);
        for(Cclasses cclassesSon : cclasses) {
            if (newClasses.equals(cclassesSon)) {
                return "fail";
            }
        }
        cclassesMapper.add(newClasses);
        return "success";
    }

}
