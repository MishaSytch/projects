﻿using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;

namespace Program
{
    public class WordCounter
    {
        private string _path;
        private string _name;
        private string _regexMask = @"[а-яa-zА-ЯA-Z]+[-']?[а-яa-zА-ЯA-Z]+|[а-яa-zА-ЯA-Z]+";
        private List<Info> _infos = new List<Info>();
        
        public class Info
        {
            public string Word { get;}
            public long Quantity { get; set; }

            public Info(string word)
            {
                Quantity = 1;
                Word = word;
            }

            public void AddToFrequency()
            {
                Quantity++;
            }
        }

        public WordCounter(string path, string name)
        {
            _path = path;
            _name = name;

        }

        public void Count()
        {
            try
            {
                using (StreamReader streamReader = new StreamReader($"{_path}/{_name}"))
                {
                    string line;
                    while ((line = streamReader.ReadLine()) != null)
                    {
                        string[] strings = line.Trim().Split(' ');
                        for (int i = 0; i < strings.Length; i++)
                        {
                            Regex regex = new Regex(_regexMask, RegexOptions.IgnoreCase);
                            if (!regex.IsMatch(strings[i])) continue;
                            string variable = regex.Match(strings[i]).ToString();
                            bool exist = false;
                            foreach (var info in _infos)
                                if (info.Word.Equals(variable.ToLower()))
                                {
                                    info.AddToFrequency();
                                    exist = true;
                                    break;
                                }

                            if (!exist && variable.Length > 0) _infos.Add(new Info(variable.ToLower()));
                        }
                    }
                }

                Save();
                Console.WriteLine("Файл Count.txt с информацией о тексте создан без ошибок");
            }
            catch (Exception e)
            {
                ConsoleColor def = Console.ForegroundColor;
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"Ошибка выполнения\n{e.Message}");
                Console.ForegroundColor = def;
            }
        }

        private void Save()
        {
            string text = DictToString();
            using (StreamWriter streamWriter = File.CreateText($"{_path}/count.txt")) streamWriter.WriteLine(text);
        }

        private string DictToString()
        {
            var infos = _infos.OrderByDescending(s => s.Quantity).ThenBy(s => s.Word);
            string str = "";
            foreach (var variable in infos) str += String.Format("{0,-24} {1}\n", variable.Word, variable.Quantity);
            return str;
        }
    }
}